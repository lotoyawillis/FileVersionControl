import {Component} from '@angular/core';
import {ApiResponse, ApiService, Dictionary} from '../../../services/api-service';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';


@Component({
  selector: 'app-restore-component',
  templateUrl: './restore-component.html',
  styleUrl: './restore-component.css',
  standalone: false
})
export class RestoreComponent {
  constructor(private apiService: ApiService, private router: Router) {}

  submit(form: NgForm) {
    if (!form.valid) {
      return;
    }

    //const vcPath = form.value.vcPath;
    //const destinationPath = form.value.destinationPath;

    const paths: Dictionary<string> = {
      'vcPath': form.value.vcPath,
      'destinationPath': form.value.destinationPath
    };

    this.apiService.postRestore(paths).subscribe(
      (response: ApiResponse) => {
        void this.router.navigate(['/success'], {state: {data: response}});
      },
      (error) => {
        const data = error.error || error;
        void this.router.navigate(['/request_error'], { state: { data } });
      }
    );
  }
}
