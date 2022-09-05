import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PasswordValidator } from 'src/app/core/shared/validators/password-validator.component';
import { User } from '../models/user';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettingsComponent implements OnInit {

  public form: FormGroup;
  public errors: any[] = [];
  public carregando: boolean = false;
  public user: User = new User;;

  constructor(private fb: FormBuilder,
    private sessionService: SessionService,
  ) { }

  ngOnInit(): void {
    var _user = JSON.parse(sessionStorage.getItem('hubmap.user')!);
    if (_user) {
      console.log(_user)
      this.user.name = _user.name;
      this.user.email = _user.email;
      this.user.nick = _user.nick;
    }

    this.form = this.fb.group({
      name: ['', [Validators.required]],
      nick: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100), PasswordValidator('password')]]
    });

    this.preencherFormEvento()
  }

  preencherFormEvento(): void {
    this.form.patchValue({
      name: this.user.name,
      nick: this.user.email,
    });
  }

  editUser() {
    if (this.form.dirty && this.form.valid) {
      var user = JSON.parse(sessionStorage.getItem('hubmap.user')!)
      if (user) {
        this.carregando = true;
        let form = Object.assign({}, new User, this.form.value);
        let p = new User;
        p.name = form.name;
        p.nick = form.nick;
        p.password = form.password;
        p.email = user.email!;

        this.sessionService.createUser(p)
          .subscribe({
            next: result => {
              this.carregando = false;
            },
            error: error => {
              this.carregando = false;
            }
          });
      }
    }
  }
}
