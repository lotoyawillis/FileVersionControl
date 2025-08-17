
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
    'index.csr.html': {size: 490, hash: '1fb64f72c462f242b14537781e9fc3cb90a2fdb7b0bde2cf91af04f4ab3e40b3', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 892, hash: '352f7b723ece6842f2c16bca4ec61ca76fa4c410b08b0e4681bca7f07928c4ff', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'index.html': {size: 1669, hash: '69ac2cc1891e8f7fbffca8c11f30c8df0c6891b51791fdba6a5ed00cd8b731cc', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)},
    'restore/index.html': {size: 2338, hash: 'f99fd74acbec6376621de3e08f48b008192cb74d9d51e681b31851cea7504544', text: () => import('./assets-chunks/restore_index_html.mjs').then(m => m.default)},
    'request_error/index.html': {size: 1570, hash: '30bc0639818c3aab561f7af845b0d83dc0cf73c0f95c6491d53e3b24b5d48340', text: () => import('./assets-chunks/request_error_index_html.mjs').then(m => m.default)},
    'commit/index.html': {size: 1989, hash: '26ff4e0b6a3b171ff4cf03b1c6875c32034551d623617356e333116c55aebf9c', text: () => import('./assets-chunks/commit_index_html.mjs').then(m => m.default)},
    'success/index.html': {size: 1555, hash: '4cd75fb627b7b07c2a61e3aac66ccbd989dff905e7d3ba5c9eaf939d4559fa0d', text: () => import('./assets-chunks/success_index_html.mjs').then(m => m.default)},
    'styles-ZUISL3KO.css': {size: 48, hash: 'OMjhBbaoui0', text: () => import('./assets-chunks/styles-ZUISL3KO_css.mjs').then(m => m.default)}
  },
};
