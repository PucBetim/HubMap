import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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

  constructor(
    private fb: FormBuilder,
    private sessionService: SessionService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required]],
      nick: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      confirmEmail: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]]
    });
  }

  createUser() {
    if (this.form.dirty && this.form.valid) {
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
          },
          error: error => {
            console.log(error.error);
          }
        });
    }
  }

  onCreate(result: any) {
    console.log(result)
  }
}
