import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommitComponent } from './api/components/commit-component/commit-component';
import { HomepageComponent } from './components/homepage-component/homepage-component';
import { routes} from './app.routes';


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
