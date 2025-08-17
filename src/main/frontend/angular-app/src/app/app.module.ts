import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HomepageComponent} from './api/features/home/homepage-component/homepage-component';
import {ApiModule} from './api/api.module';
import {AppRoutingModule} from './app-routing.module';
import {App} from './app';
import {HttpClientModule} from '@angular/common/http';
import {CommitComponent} from './api/features/commit/commit-component/commit-component';
import {FormsModule} from '@angular/forms';
import {ResultsSuccessComponent} from './api/shared/results-success-component/results-success-component';
import {ResultsErrorComponent} from './api/shared/results-error-component/results-error-component';
import {RestoreComponent} from './api/features/restore/restore-component/restore-component';

@NgModule({
  declarations: [
    HomepageComponent,
    CommitComponent,
    RestoreComponent,
    ResultsSuccessComponent,
    ResultsErrorComponent
  ],
  imports: [
    App,
    AppRoutingModule,
    BrowserModule,
    ApiModule,
    FormsModule,
    HttpClientModule
  ],
  bootstrap: [
    HomepageComponent]
})
export class AppModule { }
