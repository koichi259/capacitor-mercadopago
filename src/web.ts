import { WebPlugin } from '@capacitor/core';
import { MercadoPagoPlugin } from './definitions';

export class MercadoPagoWeb extends WebPlugin implements MercadoPagoPlugin {
  constructor() {
    super({
      name: 'MercadoPago',
      platforms: ['web']
    });
  }

  async checkout(options: { publicKey: string; preferenceId: string; }): Promise<{}> {
    return options;
  }
}

const MercadoPago = new MercadoPagoWeb();

export { MercadoPago };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(MercadoPago);
