
  Pod::Spec.new do |s|
    s.name = 'CapacitorMercadopago'
    s.version = '0.0.1'
    s.summary = 'Native integration of Mercadopago for capacitor'
    s.license = 'MIT'
    s.homepage = 'git@github.com:koichi259/capacitor-mercadopago.git'
    s.author = 'Brian Tassi'
    s.source = { :git => 'git@github.com:koichi259/capacitor-mercadopago.git', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end