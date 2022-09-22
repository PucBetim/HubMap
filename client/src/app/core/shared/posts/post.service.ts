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

  post(post: Post): Observable<Post> {

    post = new Post
    post.title = "teste";
    post.description = "descrição teste";
    post.map = "mapa teste";
    post.isPrivate = false;

    return this.http.post<Post>((ConfigService.getUrlApi() + this.userUrl), post, ConfigService.getOptions()).pipe(catchError(super.serviceError));
  };
}
