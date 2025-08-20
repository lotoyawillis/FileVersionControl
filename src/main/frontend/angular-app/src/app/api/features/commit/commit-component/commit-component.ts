import {Component} from '@angular/core';
import {ApiResponse, ApiService, Dictionary} from '../../../services/api-service';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';


@Component({
  selector: 'app-commit-component',
  templateUrl: './commit-component.html',
  styleUrl: './commit-component.css',
  standalone: false
})
export class CommitComponent {
  constructor(private apiService: ApiService, private router: Router) {}

  submit(form: NgForm) {
    if (!form.valid) {
      return;
    }

    const directoryPath: Dictionary<string> = {
      'directoryPath': form.value.directoryPath
    };
    //const path = {directoryPath};

    this.apiService.postCommit(directoryPath).subscribe(
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
