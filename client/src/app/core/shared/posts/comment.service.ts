import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable } from "rxjs";
import { BaseService } from "../../services/base.service";
import { ConfigService } from "../../services/config.service";
import { Comment } from './comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService extends BaseService {

  public userUrl = "hubmap/comments";

  constructor(private http: HttpClient,) { super(); }

  postComment(comment: Comment, id: number): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrl + `?post=${id}`), comment, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  postReply(comment: Comment): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrl), comment, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPostComments(id: number): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrl + `?post=${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getCommentById(id: number): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrl + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  updateComment(comment: Comment, id: number): Observable<any> {
    return this.http.put<any>((ConfigService.getUrlApi() + this.userUrl + `/${id}`), comment, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  deleteComment(id: number): Observable<any> {
    return this.http.delete<any>((ConfigService.getUrlApi() + this.userUrl + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  likeComment(rating: boolean, id: number): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrl + `/${id}/likes?add=${rating}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  dislikeComment(rating: boolean, id: number): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrl + `/${id}/dislikes?add=${rating}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
}