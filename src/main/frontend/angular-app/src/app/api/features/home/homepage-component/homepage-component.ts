import {Component, inject} from '@angular/core';
import {ApiService} from '../../../services/api-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-homepage-component',
  templateUrl: './homepage-component.html',
  styleUrl: './homepage-component.css',
  standalone: false
})
export class HomepageComponent {
  constructor(private apiService: ApiService, private router: Router) {
    this.router = inject(Router);
  }

  callCommit() {
    void this.router.navigateByUrl('commit');
  }

  callRestore() {
    void this.router.navigateByUrl('restore');
  }
}
