import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as tf from '@tensorflow/tfjs-node';
import * as path from 'path';

@Injectable()
export class HealthService {
  constructor(private configService: ConfigService) {}

  private model: tf.LayersModel | null = null;

  private fields = [
    'sex',
    'age',
    'HE_sbp',
    'HE_dbp',
    'HE_BMI',
    'total_sleep',
    'sm_present',
    'pa_walk',
  ];

  async loadModel() {
    if (!this.model) {
      const modelPath = path.resolve(
        __dirname,
        '../../predict_model/model.json',
      );
      this.model = await tf.loadLayersModel(`file://${modelPath}`);
    }
  }

  normalize(data: { [key: string]: number }): number[] {
    return this.fields.map((field) => {
      const value = Math.pow(data[field], 2);

      const mean = this.configService.get<number>(
        `MEAN_${field.toUpperCase()}`,
      );
      const std = this.configService.get<number>(`STD_${field.toUpperCase()}`);
      const normalizedValue = (value - mean) / std;

      return normalizedValue;
    });
  }

  async predictHealthInfo(data: { [key: string]: number }) {
    await this.loadModel();

    const inputData = tf.tensor3d(
      [this.normalize(data).map((value) => [value])],
      [1, this.fields.length, 1],
    );

    const outputData = this.model.predict(inputData) as tf.Tensor;
    const prediction_result = await outputData.data();

    return { prediction_result: prediction_result[0] };
  }
}
