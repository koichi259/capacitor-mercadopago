import { WebPlugin } from '@capacitor/core';
import { MercadoPagoPlugin } from './definitions';

export class MercadoPagoWeb extends WebPlugin implements MercadoPagoPlugin {
  constructor() {
    super({
      name: 'MercadoPago',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const MercadoPago = new MercadoPagoWeb();

export { MercadoPago };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(MercadoPago);
