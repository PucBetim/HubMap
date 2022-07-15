import { PageNotFoundComponent } from './../core/page-not-found/page-not-found.component';
import { UserSettingsComponent } from './user-settings/user-settings.component';
import { LoginComponent } from './login/login.component';
import { Routes } from '@angular/router';

export const UserRoutes: Routes = [
  { path: '', component: PageNotFoundComponent },
  { path: 'login', component: LoginComponent },
  { path: 'settings/:id', component: UserSettingsComponent}
];
