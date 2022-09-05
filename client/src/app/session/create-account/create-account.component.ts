import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EmailValidator } from 'src/app/core/shared/validators/email-validator.component';
import { PasswordValidator } from 'src/app/core/shared/validators/password-validator.component';
import { User } from '../models/user';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public carregando: boolean = false;

  constructor(
    private fb: FormBuilder,
    private sessionService: SessionService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    sessionStorage.clear();
    this.form = this.fb.group({
      name: ['', [Validators.required]],
      nick: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      confirmEmail: ['', [Validators.required, Validators.email, EmailValidator('email')]],
      password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100), PasswordValidator('password')]]
    });
  }

  createUser() {
    if (this.form.dirty && this.form.valid) {
      this.carregando = true;
      let form = Object.assign({}, new User, this.form.value);

      let p = new User;
      p.name = form.name;
      p.nick = form.nick;
      p.email = form.email;
      p.password = form.password;

      console.log(p);
      
      this.sessionService.createUser(p)
        .subscribe({
          next: result => {
            this.onCreate(result);
            this.carregando = false;
          },
          error: error => {
            this.carregando = false;
          }
        });
    }
  }

  onCreate(result: any) {
    this.router.navigate(['/session/signin']);
  }
}
