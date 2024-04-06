import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class UserModel {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  nickname: string;

  @Column({
    unique: true,
  })
  email: string;

  @Column()
  password: string;
}
