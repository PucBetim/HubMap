import { Router } from '@angular/router';
import { PostService } from '../../core/shared/posts/post-blocks.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { User } from '../models/user';
import { SessionService } from '../session.service';
import { ConfigService } from 'src/app/core/services/config.service';
import { Post } from 'src/app/core/shared/posts/post';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from 'src/app/map-creator/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettingsComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public loading: boolean = false;
  public user: User = new User;
  public posts: Post[];
  public results: boolean = false;

  constructor(private fb: FormBuilder,
    private sessionService: SessionService, private postService: PostService,
    public router: Router,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    var _user = ConfigService.getUser();
    if (_user) {
      this.user.name = _user.name;
      this.user.email = _user.email;
      this.user.nick = _user.nick;
    }

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(80)]],
      nick: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
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
    this.loading = true;
    this.postService.getUserPosts().subscribe(
      {
        next: result => {
          this.posts = result.body;
          this.loading = false;
        },
        error: error => {
          this.snackBar.open("Falha ao obter mapas! Tente novamente mais tarde.", "Ok", {
            duration: 2000
          });
          this.loading = false;
        }
      })
    this.results = true;
  }

  editUser() {
    if (this.form.dirty && this.form.valid && this.form.dirty) {
      var user = JSON.parse(localStorage.getItem('hubmap.user')!)
      if (user) {
        this.loading = true;
        let form = Object.assign({}, new User, this.form.value);
        let p = new User;
        p.name = form.name;
        p.nick = form.nick;
        p.email = user.email!;
        this.sessionService.updateUser(p)
          .subscribe({
            next: result => {
              this.loading = false;
              this.getUser();
            },
            error: error => {
              this.snackBar.open("Falha ao salvar dados de usuário! Tente novamente mais tarde.", "Ok", {
                duration: 2000
              });
              this.loading = false;
            }
          });
      }
    }
  }

  getUser() {
    this.loading = true;
    this.sessionService.getUserLogado().subscribe({
      next: result => {
        localStorage.setItem('hubmap.user', JSON.stringify(result.body));
        this.snackBar.open("Usuário atualizado com sucesso!", "Ok", {
          duration: 2000
        });
        this.loading = false;
      },
      error: erro => {
        this.snackBar.open("Falha ao atualizar o usuário! Tente novamente mais tarde.", "Ok", {
          duration: 2000
        });
        this.loading = false;
      }
    })
  }

  logout() {
    var confirmDeleteConfig = {
      disableClose: false,
      width: 'auto',
      data: {
        titulo: "Sair",
        texto: "Tem certeza que deseja sair?"
      }
    };

    const dialogRef = this.dialog.open(ConfirmDialogComponent, confirmDeleteConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        localStorage.clear();
        this.router.navigate(['']);
      }
    });

  }
}
