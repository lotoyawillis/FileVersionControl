import {Component, inject, Injectable} from '@angular/core';
import {ApiResponse, ApiService} from '../../../services/api-service';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';


@Component({
  selector: 'app-restore-component',
  templateUrl: './restore-component.html',
  styleUrl: './restore-component.css',
  standalone: false
})
@Injectable({
  providedIn: 'root'
})
export class RestoreComponent {
  constructor(private apiService: ApiService, private router: Router) {
    this.router = inject(Router);
  }

  submit(form: NgForm) {
    const vcPath = form.value.vcPath;
    const destinationPath = form.value.destinationPath;

    const paths = [vcPath, destinationPath];

    this.apiService.postRestore(paths).subscribe(
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
