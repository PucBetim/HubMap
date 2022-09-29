import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable } from "rxjs";
import { BaseService } from "../../services/base.service";
import { ConfigService } from "../../services/config.service";
import { Map } from "./map"
import { Post } from "./post";

@Injectable({
  providedIn: 'root'
})
export class PostService extends BaseService {

  public userUrl = "hubmap/posts";

  constructor(private http: HttpClient,) { super(); }

  post(post: Post): Observable<any> {

    post = new Post
    post.title = "teste";
    post.description = "descrição teste";
    post.map = null;
    post.isPrivate = false;

    return this.http.post<any>((ConfigService.getUrlApi() + this.userUrl), post, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };

  getUserPosts(): Observable<any> {
    return this.http.get<any>((ConfigService.getUrlApi() + this.userUrl), ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
}
