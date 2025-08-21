
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
    'index.csr.html': {size: 490, hash: '0f6e94cffe69044ba0433f717296055400d9d0881d37d3f030d310067af66aa5', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 892, hash: 'b7d7fcedccfdefbf79497264e56cfd509aa810d1e77c40e9cc2704c9ba81305f', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'index.html': {size: 1669, hash: 'c5aa94881625c05281ab62cdc322406d9ef0acd44e9156b07f20c2ef9241840b', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)},
    'success/index.html': {size: 1555, hash: '9d2bf84f59fbd16495998b2a91f7a170c7d7e776e5a285c54b4689bb6a4ab206', text: () => import('./assets-chunks/success_index_html.mjs').then(m => m.default)},
    'restore/index.html': {size: 2397, hash: 'b32e7a4cb914423f0e0ac06fb0c1138b5f8a7f140c8ed084e0e3c77124dde549', text: () => import('./assets-chunks/restore_index_html.mjs').then(m => m.default)},
    'commit/index.html': {size: 2031, hash: 'a972d70d744ce05fafabb57c322dc5b12d773c8c26354d6e75b49a24844ce095', text: () => import('./assets-chunks/commit_index_html.mjs').then(m => m.default)},
    'request_error/index.html': {size: 1570, hash: '387cffea2e7a0a6bae144b8c44cd667d6307eeb8e88ef883088ecae04626cdb6', text: () => import('./assets-chunks/request_error_index_html.mjs').then(m => m.default)},
    'styles-ZUISL3KO.css': {size: 48, hash: 'OMjhBbaoui0', text: () => import('./assets-chunks/styles-ZUISL3KO_css.mjs').then(m => m.default)}
  },
};
