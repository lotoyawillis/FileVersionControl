
export default {
  bootstrap: () => import('./main.server.mjs').then(m => m.default),
  inlineCriticalCss: true,
  baseHref: '/',
  locale: undefined,
  routes: [
  {
    "renderMode": 2,
    "route": "/"
  },
  {
    "renderMode": 2,
    "route": "/commit"
  },
  {
    "renderMode": 2,
    "route": "/restore"
  },
  {
    "renderMode": 2,
    "route": "/success"
  },
  {
    "renderMode": 2,
    "route": "/request_error"
  },
  {
    "renderMode": 2,
    "redirectTo": "/",
    "route": "/**"
  }
],
  entryPointToBrowserMapping: undefined,
  assets: {
    'index.csr.html': {size: 490, hash: '74b4ea9edc20cff0fff2b29e17d19c4df391fe41f63b6051d701adb0ee940fc1', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 892, hash: 'f4281411ecf46906df275157e1d1acab1c2e11cc16a21a1cdee1eeb09e04015f', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'success/index.html': {size: 1555, hash: '37ec1ab6ae9f1759acb5f398ad14e03e9fa8568a2087bf065b76cab94c19a076', text: () => import('./assets-chunks/success_index_html.mjs').then(m => m.default)},
    'index.html': {size: 1669, hash: '3884906e9a18689777a4a1f5e65d1807c1e54d9adac6d090ac59b0927561c8ce', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)},
    'commit/index.html': {size: 2031, hash: 'cad89b5c5ec643162f8deb96904111d46ae2a96fe4a2875251a6ed69eeb32acb', text: () => import('./assets-chunks/commit_index_html.mjs').then(m => m.default)},
    'request_error/index.html': {size: 1570, hash: 'ae40ff469df61f5a07325c1d1eb282d53b4dad7469237ffaa64e553a98967fb1', text: () => import('./assets-chunks/request_error_index_html.mjs').then(m => m.default)},
    'restore/index.html': {size: 2397, hash: '76fd99e7e7b5de872dcfe2dbd297558a8fa66be1db476c8773365b90fdc3738f', text: () => import('./assets-chunks/restore_index_html.mjs').then(m => m.default)},
    'styles-ZUISL3KO.css': {size: 48, hash: 'OMjhBbaoui0', text: () => import('./assets-chunks/styles-ZUISL3KO_css.mjs').then(m => m.default)}
  },
};
