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

  public userUrlComments = "hubmap/comments";
  public userUrlPublicComments = "hubmap/public/comments";

  constructor(private http: HttpClient,) { super(); }

  postComment(comment: Comment, id: string): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlComments + `?post=${id}`), comment, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  postReply(comment: Comment): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlComments), comment, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPostComments(id: string): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlComments + `?post=${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getCommentById(id: number): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlComments + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  updateComment(comment: Comment, id: number): Observable<any> {
    return this.http.put<any>((ConfigService.getUrlApi() + this.userUrlComments + `/${id}`), comment, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  deleteComment(id: number): Observable<any> {
    return this.http.delete<any>((ConfigService.getUrlApi() + this.userUrlComments + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  // Rate Comment
  likeComment(rating: boolean, id: number): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlComments + `/${id}/likes?add=${rating}`), null, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  dislikeComment(rating: any, id: number): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlComments + `/${id}/dislikes?add=${rating}`), null, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  // End Rate Comment

  // Public Comments
  getPublicPostComments(id: string): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPublicComments + `?post=${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  // End Public Comments
}