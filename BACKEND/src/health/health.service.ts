import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as tf from '@tensorflow/tfjs-node';
import * as path from 'path';

@Injectable()
export class HealthService {
  constructor(private configService: ConfigService) {}
  private model: tf.LayersModel | null = null;

  async loadModel() {
    if (!this.model) {
      const modelPath = path.resolve(__dirname, '../../test_model/model.json');
      this.model = await tf.loadLayersModel(`file://${modelPath}`);
    }
  }

  normalize(data: { [key: string]: number }): number[] {
    const normalizedData = Object.values(data).map((value, index) => {
      const fieldName = Object.keys(data)[index];
      const min = this.configService.get<number>(
        `MIN_${fieldName.toUpperCase()}`,
      );
      const max = this.configService.get<number>(
        `MAX_${fieldName.toUpperCase()}`,
      );
      return (value - min) / (max - min);
    });

    return normalizedData;
  }

  async predictHealthInfo(data: { [key: string]: number }) {
    await this.loadModel();

    const inputData = this.normalize(data);

    const outputData = this.model.predict(
      tf.tensor2d([inputData], [1, 9]),
    ) as tf.Tensor;
    const prediction_result = await outputData.data();

    return prediction_result[0];
  }
}
