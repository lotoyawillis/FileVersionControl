import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//import { PostListComponent } from './post-list/post-list.component';
import { CommitComponent } from './components/commit-component/commit-component';
import {FormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [// CommitComponent
  ],
  imports: [CommonModule, FormsModule, HttpClientModule]
  // ,exports: [CommitComponent]
})
export class ApiModule { }
