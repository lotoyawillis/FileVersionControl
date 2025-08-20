import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface ApiResponse {
  status: number;
  results: string[];
  message: string | null;
}

export interface Dictionary<T> {
  [key: string]: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) {
  }

  postCommit(body: Dictionary<string>): Observable<ApiResponse> {
    console.log(body)
    const apiUrl = `${this.baseUrl}commit`;
    return this.http.post<ApiResponse>(apiUrl, body);
  }

  postRestore(body: Dictionary<string>): Observable<ApiResponse> {
    console.log(body)
    const apiUrl = `${this.baseUrl}restore`;
    return this.http.post<ApiResponse>(apiUrl, body);
  }
}
