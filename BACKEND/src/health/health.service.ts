import { Injectable } from '@nestjs/common';
import * as tf from '@tensorflow/tfjs-node';
import * as path from 'path';

@Injectable()
export class HealthService {
  private model: tf.LayersModel | null = null;

  async loadModel() {
    if (!this.model) {
      const modelPath = path.resolve(__dirname, '../../test_model/model.json');
      this.model = await tf.loadLayersModel(`file://${modelPath}`);
    }
  }

  async predictHealthInfo(
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
    await this.loadModel();
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

    const outputData = this.model.predict(
      tf.tensor2d([inputData], [1, 9]),
    ) as tf.Tensor;
    const prediction_result = await outputData.data();

    return prediction_result[0];
  }
}
