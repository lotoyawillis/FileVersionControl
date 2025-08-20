
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
    'index.csr.html': {size: 490, hash: '2886b6af3ee69f803f5002592587d2f66fb1e4781a0754a28f12fbbd49d3dfdb', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 892, hash: '0533dd5f34be07d2717a67620fe47fb599f4216f4e02dafc3c541ff5d8783c80', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'index.html': {size: 1669, hash: 'b7254434b88e31144f440bd0e0789e7b13ce882459c5ec809bcecba787168a24', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)},
    'commit/index.html': {size: 2031, hash: 'cc41b42c9381022141d3301cf1ac26a29281f52732b6c6128d35090e3fad2bfc', text: () => import('./assets-chunks/commit_index_html.mjs').then(m => m.default)},
    'request_error/index.html': {size: 1570, hash: '5110fef3df50fcea69e0d4124011e4da5f7cc74dc293fb1df7a2de50d2aada89', text: () => import('./assets-chunks/request_error_index_html.mjs').then(m => m.default)},
    'restore/index.html': {size: 2397, hash: 'eed037aaf32312c09e51dfb04b13bec6fa410f7c8f716329a988ab6f36a29313', text: () => import('./assets-chunks/restore_index_html.mjs').then(m => m.default)},
    'success/index.html': {size: 1555, hash: 'c632aae07792d56aae50a9ec0a6bb746f13d68d839fc18c44843f5b189560f1f', text: () => import('./assets-chunks/success_index_html.mjs').then(m => m.default)},
    'styles-ZUISL3KO.css': {size: 48, hash: 'OMjhBbaoui0', text: () => import('./assets-chunks/styles-ZUISL3KO_css.mjs').then(m => m.default)}
  },
};
