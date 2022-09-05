import { Login } from '../models/login';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SessionService } from '../session.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public loginForm: Login;
  public carregando: boolean = false;

  constructor(
    private fb: FormBuilder,
    private sessionService: SessionService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    sessionStorage.clear();
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]]
    });
  }

  login() {
    this.carregando = true;
    if (this.form.dirty && this.form.valid) {
      let p = Object.assign({}, this.loginForm, this.form.value);
      this.sessionService.login(p)
        .subscribe({
          next: result => {
            this.onLogin(result);
            this.carregando = false;
          },
          error: error => {
            this.errors = ["E-mail ou senha inválidos!"];
            this.carregando = false;
          }
        });
    }
  }

  onLogin(result: any) {
    sessionStorage.setItem('hubmap.token', result.headers.get('Authorization'));
    this.sessionService.getUserLogado().subscribe(
      {
        next: result => {
          sessionStorage.setItem('hubmap.user', JSON.stringify(result.body));
          this.router.navigate(['/']);
        },
        error: error => {
          console.log(error)
        }
      })
  }
}
