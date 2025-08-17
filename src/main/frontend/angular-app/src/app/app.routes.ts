import { Routes } from '@angular/router';
import { CommitComponent } from './api/features/commit/commit-component/commit-component';
import { HomepageComponent } from './api/features/home/homepage-component/homepage-component';
import {ResultsSuccessComponent} from './api/shared/results-success-component/results-success-component';
import {ResultsErrorComponent} from './api/shared/results-error-component/results-error-component';
import {RestoreComponent} from './api/features/restore/restore-component/restore-component';


export const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'commit', component: CommitComponent },
  { path: 'restore', component: RestoreComponent },
  { path: 'success', component: ResultsSuccessComponent },
  { path: 'request_error', component: ResultsErrorComponent },
  { path: '**', redirectTo: '' }
];
