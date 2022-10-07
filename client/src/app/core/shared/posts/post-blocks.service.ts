import { Block, Post } from 'src/app/core/shared/posts/post';
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable } from "rxjs";
import { BaseService } from "../../services/base.service";
import { ConfigService } from "../../services/config.service";

@Injectable({
  providedIn: 'root'
})
export class PostService extends BaseService {

  public userUrlPost = "hubmap/posts";
  public userUrlPublicPost = "hubmap/public/posts";
  public userUrlBlocks = "hubmap/blocks";

  constructor(private http: HttpClient,) { super(); }

  //Posts
  postPost(post: Post): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlPost), post, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getUserPosts(): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPost), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPostById(id: number): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  updatePost(post: Post, id: number): Observable<any> {
    return this.http.put<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}`), post, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  deletePost(id: number): Observable<any> {
    return this.http.delete<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //

  // Public post
  getPublicPostsById(id: number): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPublicPost + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPublicPosts(): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPublicPost), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //

  // Blocks
  postBlocks(map: Block, id: number): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlBlocks + `?post=${id}`), map, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPostBlocks(id: number): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlBlocks + `?post=${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  updateBlocks(blocks: Block, id: number): Observable<any> {
    return this.http.put<any>((ConfigService.getUrlApi() + this.userUrlBlocks + `?post=${id}`), blocks, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
}
  //
