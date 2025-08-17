
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
    'index.csr.html': {size: 490, hash: '7ccb27714edd687fcdf2d7b246667c894ea4fc67284184576941c4dab1324c5a', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 892, hash: '8a3287f33f24bba2e06a1f62996901a4bd3dab62363362b09f4cb5ba46838355', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'index.html': {size: 1669, hash: '37de8ddf4e3a90ca39ece450ba9df4baa4bd1dd5814940ea4a35ddf9c668459e', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)},
    'request_error/index.html': {size: 1570, hash: 'd03ddb882f84fcd1ef4223cadc2f8ccdda69004cf0d88edf10c709f462f7179b', text: () => import('./assets-chunks/request_error_index_html.mjs').then(m => m.default)},
    'commit/index.html': {size: 1989, hash: '6516520d4dfcf6be48723af14d901b52c43838b80f85dbe1d1dd604c041e59ed', text: () => import('./assets-chunks/commit_index_html.mjs').then(m => m.default)},
    'success/index.html': {size: 1555, hash: '7c3230bdf67d007653603c3d13ff64667289035d9d9d7abe2ed0005a96e3cac3', text: () => import('./assets-chunks/success_index_html.mjs').then(m => m.default)},
    'restore/index.html': {size: 2338, hash: '25184944fa5c3c9b55ab510bc5ad7469d2f7388e61932c8b2d077f549b00902c', text: () => import('./assets-chunks/restore_index_html.mjs').then(m => m.default)},
    'styles-ZUISL3KO.css': {size: 48, hash: 'OMjhBbaoui0', text: () => import('./assets-chunks/styles-ZUISL3KO_css.mjs').then(m => m.default)}
  },
};
