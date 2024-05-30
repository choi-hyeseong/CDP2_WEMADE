import { Module } from '@nestjs/common';
import { HealthService } from './health.service';
import { HealthController } from './health.controller';
import { ConfigModule } from '@nestjs/config';
import { AuthModule } from 'src/auth/auth.module';

@Module({
  imports: [ConfigModule.forRoot(), AuthModule],
  controllers: [HealthController],
  providers: [HealthService],
})
export class HealthModule {}
