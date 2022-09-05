import { Login } from '../models/login';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SessionService } from '../session.service';
import { Router } from '@angular/router';
import { ConfigService } from 'src/app/core/services/config.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public loginForm: Login;

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
    if (this.form.dirty && this.form.valid) {
      let p = Object.assign({}, this.loginForm, this.form.value);
      this.sessionService.login(p)
        .subscribe({
          next: result => {
            this.onLogin(result);
          },
          error: error => {
            this.errors = ["E-mail ou senha inválidos!"];
          }
        });
    }
  }

  onLogin(result: any) {
    sessionStorage.setItem('hubmap.jwt', result.headers.get('Authorization'));
    this.router.navigate(['/']);
  }
}
