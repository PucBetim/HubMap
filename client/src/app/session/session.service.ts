import { User } from './models/user';
import { BaseService } from './../core/services/base.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { ConfigService } from '../core/services/config.service';
import { Login } from './models/login';

@Injectable({
  providedIn: 'root'
})
export class SessionService extends BaseService {

  constructor(private http: HttpClient) { super(); }
  // 'Authorization': sessionStorage.getItem('hubmap.token')!

  options = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    }),
    observe: "response" as 'body',
  }

  
  login(login: Login): Observable<Login> {
    return this.http.post<Login>((ConfigService.getUrlApi() + "login"), login, this.options).pipe(catchError(super.serviceError));
  };

  createUser(user: User): Observable<User> {
    return this.http.post<User>(ConfigService.getUrlApi() + "hubmap/appUsers", user, this.options).pipe(catchError(super.serviceError));
  };

    getUserLogado(): Observable<any> {
    return this.http.get<any>('https://virtserver.swaggerhub.com/JUNIOFERREIRADEALMEI/HubMap/1.0.0/hubmap/appUsers', this.options).pipe(catchError(super.serviceError));
  };

  // getUserLogado(): Observable<any> {
  //   let header = new HttpHeaders().set('Authorization', 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0ZUBnbWFpbC5jb20iLCJleHAiOjE2NjI0NzM5ODQsIm5pY2siOiJ0ZXN0ZSJ9.QdTUoyMscpGfrJSXVjThyZKr_HRcsjUOFAw8-EewBLUWGs079-QykzajnlmnGkMlnza7tYXvBp5Pyx0QTieTQg'); 
  //   return this.http.get<any>('https://hub-map-server.herokuapp.com/hubmap/appUsers', {
  //     headers: header,
  //     observe: "response" as "body"
  //   }).pipe(catchError(super.serviceError));
  // };
}
