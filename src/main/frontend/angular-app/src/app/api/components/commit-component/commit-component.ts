import {Component, Injectable} from '@angular/core';
import {ApiService} from '../../services/api-service';

@Component({
  selector: 'app-commit-component',
  // imports: [],
  templateUrl: './commit-component.html',
  styleUrl: './commit-component.css',
  standalone: false
})
@Injectable({
  providedIn: 'root'
})
export class CommitComponent {
  public folder: String;

  constructor(private apiService: ApiService) {
    this.folder = "";
  }

  /*
  async browse() {
    const result = await dialog.showOpenDialog({title: 'Select a folder', properties: ['openDirectory']});
      if (result.canceled) {
        console.log('User canceled the dialog');
      }
      else {
        this._folder = result.filePaths[0];
      }
  }
  */
  async onDirectorySelected(event: any) {
    const files: FileList = event.target.files;

    if (files.length === 0) {return;}

    const filePath = (files[0] as any).webkitRelativePath;
    this.folder = filePath.split('/')[0];
  }

  submit(dir: String) {
    this.apiService.postCommit(dir).subscribe(
      (response) => {
        console.log('API Response:', response);
      },
      (error) => {
        console.error('API Error:', error);
      }
    );
  }

  /*
  get folder() {
    return this._folder
  }


  set folder(folder: String) {
    this._folder = folder;
  }

  */

  ngOnInit() {}
}
