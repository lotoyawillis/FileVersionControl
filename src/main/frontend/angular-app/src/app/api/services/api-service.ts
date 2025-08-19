import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface ApiResponse {
  status: number;
  results: string[];
  message: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) {}

  postCommit(body: {path: string}): Observable<ApiResponse> {
    const apiUrl = `${this.baseUrl}commit`;
    return this.http.post<ApiResponse>(apiUrl, body);
  }

  postRestore(body: string[]): Observable<ApiResponse> {
    const apiUrl = `${this.baseUrl}restore`;
    return this.http.post<ApiResponse>(apiUrl, body);
  }
}
