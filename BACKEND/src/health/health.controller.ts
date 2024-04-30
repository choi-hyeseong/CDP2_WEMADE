import { Body, Controller, Post } from '@nestjs/common';
import { HealthService } from './health.service';

@Controller('health')
export class HealthController {
  constructor(private readonly healthService: HealthService) {}

  @Post('/predict')
  async getHealthPredction(
    @Body('sex') sex: number,
    @Body('age') age: number,
    @Body('HE_sbp') HE_sbp: number,
    @Body('HE_dbp') HE_dbp: number,
    @Body('HE_BMI') HE_BMI: number,
    @Body('HE_PLS') HE_PLS: number,
    @Body('sm_present') sm_present: number,
    @Body('pa_walk') pa_walk: number,
    @Body('total_sleep') total_sleep: number,
  ) {
    return this.healthService.predictHealthInfo(
      sex,
      age,
      HE_sbp,
      HE_dbp,
      HE_BMI,
      HE_PLS,
      sm_present,
      pa_walk,
      total_sleep,
    );
  }
}
