import { Body, Controller, Post } from '@nestjs/common';
import { HealthService } from './health.service';

@Controller('health')
export class HealthController {
  constructor(private readonly healthService: HealthService) {}

  @Post('/predict')
  async getHealthPredction(@Body() data: Record<string, number>) {
    return this.healthService.predictHealthInfo(data);
  }
}
