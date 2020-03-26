declare module '@capacitor/core' {
  interface PluginRegistry {
    MercadoPago: MercadoPagoPlugin;
  }
}

export interface MercadoPagoPlugin {
  checkout(options: {
    publicKey: string;
    preferenceId: string;
  }): Promise<String>;
}
