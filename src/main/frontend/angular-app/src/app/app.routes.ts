/*
import { Routes } from '@angular/router';

export const routes: Routes = [];
*/
import { Routes } from '@angular/router';
import { CommitComponent } from './api/components/commit-component/commit-component';
import { HomepageComponent } from './components/homepage-component/homepage-component';


export const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'commit', component: CommitComponent },
  { path: 'restore', component: CommitComponent }
];
