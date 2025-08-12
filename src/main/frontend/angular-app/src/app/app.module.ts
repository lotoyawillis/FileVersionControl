import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';

// import { AppComponent } from './app.component';
import { HomepageComponent } from './components/homepage-component/homepage-component';
import { ApiModule } from './api/api.module';
import { AppRoutingModule } from './app-routing.module';
import {App} from './app';
// import {AppComponent} from './app-component/app-component';
import {HttpClientModule} from '@angular/common/http';
import {CommitComponent} from './api/components/commit-component/commit-component';
import {FormsModule} from '@angular/forms';
// import {AppComponent} from './app-component/app-component';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';

@NgModule({
  /*
  providers: [
    {
      provide: LocationStrategy,
      useClass: HashLocationStrategy
    }],
   */
  declarations: [
    // AppComponent,
    HomepageComponent,
    CommitComponent
  ],
  imports: [
    App,
    AppRoutingModule,
    BrowserModule,
    ApiModule,
    FormsModule,
    HttpClientModule
  ],
  bootstrap: [//AppComponent,
    // App,
    HomepageComponent]
})
export class AppModule { }
