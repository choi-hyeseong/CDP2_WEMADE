import { Injectable } from '@nestjs/common';
import * as tf from '@tensorflow/tfjs-node';
import * as path from 'path';

@Injectable()
export class HealthService {
  async loadAndUseModel(
    sex: number,
    age: number,
    HE_sbp: number,
    HE_dbp: number,
    HE_BMI: number,
    HE_PLS: number,
    sm_present: number,
    pa_walk: number,
    total_sleep: number,
  ) {
    const modelPath = path.resolve(__dirname, '../../test_model/model.json');
    const model = await tf.loadLayersModel(`file://${modelPath}`);

    const inputData = [
      sex,
      age,
      HE_sbp,
      HE_dbp,
      HE_BMI,
      HE_PLS,
      sm_present,
      pa_walk,
      total_sleep,
    ].map(Number);

    const outputData = model.predict(
      tf.tensor2d([inputData], [1, 9]),
    ) as tf.Tensor;
    const prediction_result = await outputData.data();

    return prediction_result[0];
  }
}
