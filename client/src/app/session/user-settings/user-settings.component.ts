import { PostService } from './../../core/shared/posts/post.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { User } from '../models/user';
import { SessionService } from '../session.service';
import { ConfigService } from 'src/app/core/services/config.service';
import { Post } from 'src/app/core/shared/posts/post';

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettingsComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public carregando: boolean = false;
  public user: User = new User;
  public posts: Post[];

  //teste
  public map = new Map;
  public results: boolean = false;

  constructor(private fb: FormBuilder,
    private sessionService: SessionService, private postService: PostService
  ) { }

  ngOnInit(): void {
    var _user = JSON.parse(sessionStorage.getItem('hubmap.user')!);
    if (_user) {
      this.user.name = _user.name;
      this.user.email = _user.email;
      this.user.nick = _user.nick;
    }

    this.form = this.fb.group({
      name: ['', [Validators.required]],
      nick: ['', [Validators.required]],
    });

    this.fillFormEvent()
    this.getUserMaps()
  }

  fillFormEvent(): void {
    this.form.patchValue({
      name: this.user.name,
      nick: this.user.nick,
    });
  }

  getUserMaps() {
    this.postService.getUserPosts().subscribe(
      {
        next: result => {
          this.posts = result.body;

          //momentâneo
          var _map = JSON.parse(localStorage.getItem('mapa') || '{}');
          if (_map.blocks){
            this.map = _map;      
            this.results = true;
            }
        },
        error: error => {
          console.log(error)
        }
      })
  }
  editUser() {
    if (this.form.dirty && this.form.valid && this.form.dirty) {
      var user = JSON.parse(sessionStorage.getItem('hubmap.user')!)
      if (user) {
        this.carregando = true;
        let form = Object.assign({}, new User, this.form.value);
        let p = new User;
        p.name = form.name;
        p.nick = form.nick;
        p.email = user.email!;
        console.log(p)
        this.sessionService.updateUser(p)
          .subscribe({
            next: result => {
              this.carregando = false;
              ConfigService.resetLogin();
            },
            error: error => {
              this.carregando = false;
            }
          });
      }
    }
  }
}
