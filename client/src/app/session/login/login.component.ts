import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Login } from './../models/login';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public loginForm: Login;

  constructor(
    private fb: FormBuilder,
    private sessionService: SessionService
  ) { }

  ngOnInit(): void {
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
            console.log(error.error);
          }
        });
    }
  }

  onLogin(result: any) {
    console.log(result)
  }
}
