
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
  }
],
  entryPointToBrowserMapping: undefined,
  assets: {
    'index.csr.html': {size: 379, hash: 'a6da003129ecd36bad32e1d37fb671f17bb003d485cdf3817398b95e063eb40a', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 892, hash: '07fdf3203530e2b0026e1114bad947aca8b975703f6123d0ae1c7dede7c8143b', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'restore/index.html': {size: 1870, hash: '17a36d76a8a26bd1d1f871ffdc1b6b62e9d6b56446c775959bbf477d87b0609e', text: () => import('./assets-chunks/restore_index_html.mjs').then(m => m.default)},
    'commit/index.html': {size: 1870, hash: '17a36d76a8a26bd1d1f871ffdc1b6b62e9d6b56446c775959bbf477d87b0609e', text: () => import('./assets-chunks/commit_index_html.mjs').then(m => m.default)},
    'index.html': {size: 1558, hash: '79d47af3248441d306b442c8a0a4b1d31b716443290cf94cee197fe847ab7937', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)},
    'styles-5INURTSO.css': {size: 0, hash: 'menYUTfbRu8', text: () => import('./assets-chunks/styles-5INURTSO_css.mjs').then(m => m.default)}
  },
};
