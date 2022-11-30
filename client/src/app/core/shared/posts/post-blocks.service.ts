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
  public userUrlSearch = "hubmap/public/search"

  constructor(private http: HttpClient,) { super(); }

  //Posts
  postPost(post: Post): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlPost), post, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getUserPosts(): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPost), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPostById(id: string): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  updatePost(post: Post, id: string): Observable<any> {
    return this.http.put<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}`), post, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  deletePost(id: string): Observable<any> {
    return this.http.delete<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //

  // Public post
  getPublicPostsById(id: string): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPublicPost + `/${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPublicPosts(size: number = 10, page: number = 0, descending:boolean = true, sort: string = "views, likes"): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlPublicPost + `?size=${size}&page=${page}&descending=${descending}&sort=${sort}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //

  // Blocks
  postBlocks(map: Block, id: string): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlBlocks + `?post=${id}`), map, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getPostBlocks(id: string): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrlBlocks + `?post=${id}`), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  updateBlocks(blocks: Block, id: string): Observable<any> {
    return this.http.put<any>((ConfigService.getUrlApi() + this.userUrlBlocks + `?post=${id}`), blocks, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //


  // Likes Dislikes
  likePost(rating: boolean, id: string): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}/likes?add=${rating}`), null, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  dislikePost(rating: boolean, id: string): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}/dislikes?add=${rating}`), null, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //

  // Post View
  viewPost(id: string): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlPost + `/${id}/views`), null, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //

  // Search Posts
  searchPosts(search: string): Observable<any> {
    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrlSearch), search, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
  //
}

