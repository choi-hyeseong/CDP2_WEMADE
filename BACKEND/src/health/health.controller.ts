import { Body, Controller, Post, UseGuards } from '@nestjs/common';
import { HealthService } from './health.service';
import { AccessTokenGuard } from 'src/auth/guard/bearer-token.guard';

@Controller('health')
export class HealthController {
  constructor(private readonly healthService: HealthService) {}

  @Post('/predict')
  @UseGuards(AccessTokenGuard)
  async getHealthPredction(@Body() data: Record<string, number>) {
    return this.healthService.predictHealthInfo(data);
  }
}
