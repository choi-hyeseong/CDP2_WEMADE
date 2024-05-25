import { Injectable, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { UserModel } from 'src/user/entities/user.entity';
import { UserService } from 'src/user/user.service';
import * as bcrypt from 'bcrypt';
import { HASH_ROUNDS, JWT_SECRET } from './const/auth.const';

@Injectable()
export class AuthService {
  constructor(
    private readonly jwtService: JwtService,
    private readonly userService: UserService,
  ) {}

  extractTokenFromHeader(header: string, isBearer: boolean) {
    const splitToken = header.split(' ');

    const prefix = isBearer ? 'Bearer' : 'Basic';

    if (splitToken.length !== 2 || splitToken[0] !== prefix) {
      throw new UnauthorizedException('잘못된 토큰');
    }

    const token = splitToken[1];

    return token;
  }

  // decodeBasicToken(base64String: string) {
  //   const decoded = Buffer.from(base64String, 'base64').toString('utf8');

  //   const split = decoded.split(':');

  //   if (split.length !== 2) {
  //     throw new UnauthorizedException('잘못된 유형의 토큰');
  //   }

  //   const email = split[0];
  //   const password = split[1];

  //   return {
  //     email,
  //     password,
  //   };
  // }

  verifyToken(token: string) {
    return this.jwtService.verify(token, {
      secret: JWT_SECRET,
    });
  }

  rotateToken(token: string, isRefreshToken: boolean) {
    const decoded = this.jwtService.verify(token, {
      secret: JWT_SECRET,
    });

    if (decoded.type !== 'refresh') {
      throw new UnauthorizedException('토큰 재발급은 refresh로만 가능');
    }

    return this.signToken(
      {
        ...decoded,
      },
      isRefreshToken,
    );
  }

  signToken(user: Pick<UserModel, 'email' | 'id'>, isRefreshToken: boolean) {
    const payload = {
      email: user.email,
      sub: user.id,
      type: isRefreshToken ? 'refresh' : 'access',
    };

    return this.jwtService.sign(payload, {
      secret: JWT_SECRET,
    });
  }

  loginUser(user: Pick<UserModel, 'email' | 'id'>) {
    return {
      accessToken: this.signToken(user, false),
      refreshToken: this.signToken(user, true),
    };
  }

  async authenticateByEmailAndPassword(
    user: Pick<UserModel, 'email' | 'password'>,
  ) {
    const existingUser = await this.userService.getUserByEmail(user.email);

    if (!existingUser) {
      throw new UnauthorizedException('존재하지 않는 사용자입니다');
    }

    const passOk = await bcrypt.compare(user.password, existingUser.password);

    if (!passOk) {
      throw new UnauthorizedException('비밀번호가 틀렸습니다');
    }

    return existingUser;
  }

  async loginWithEmail(user: Pick<UserModel, 'email' | 'password'>) {
    const existingUser = await this.authenticateByEmailAndPassword(user);

    return this.loginUser(existingUser);
  }

  async registerWithEmail(
    user: Pick<UserModel, 'nickname' | 'email' | 'password'>,
  ) {
    const hash = await bcrypt.hash(user.password, HASH_ROUNDS);

    // const newUser = await this.userService.createUser({
    //   ...user,
    //   password: hash,
    // });

    // return this.loginUser(newUser);

    await this.userService.createUser({
      ...user,
      password: hash,
    });

    return true;
  }
}
