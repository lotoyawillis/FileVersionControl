import {Component, inject, Injectable} from '@angular/core';
import {ApiResponse, ApiService} from '../../../services/api-service';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';


@Component({
  selector: 'app-commit-component',
  templateUrl: './commit-component.html',
  styleUrl: './commit-component.css',
  standalone: false
})
@Injectable({
  providedIn: 'root'
})
export class CommitComponent {
  constructor(private apiService: ApiService, private router: Router) {
    this.router = inject(Router);
  }

  submit(form: NgForm) {
    const path = form.value.path;

    this.apiService.postCommit(path).subscribe(
      (response: ApiResponse) => {
        void this.router.navigate(['/success'], {state: {data: response}});
      },
      (error) => {
        if (error.status === 304 || error.status === 400 || error.status === 500) {
          void this.router.navigate(['/request_error'], {state: {data: error.error}});
        } else {
          void this.router.navigate(['/request_error'], {state: {data: error}});
        }
      }
    );
  }

  ngOnInit() {}
}
