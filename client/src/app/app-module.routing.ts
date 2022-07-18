import { PageNotFoundComponent } from './core/page-not-found/page-not-found.component';
import { LandingComponent } from './landing/landing.component';
import { Routes } from '@angular/router';
import { HomeComponent } from './core/home-layout/home.component';

export const AppRoutes: Routes = [
  {
    path: '', component: HomeComponent,
    children: [
      { path: '', component: LandingComponent },
      { path: 'user', loadChildren: () => import('./user/user.module').then(m => m.UserModule) },
      { path: 'creator', loadChildren: () => import('./map-creator/map-creator.module').then(m => m.MapCreatorModule) },
      { path: '**', component: PageNotFoundComponent }
    ],
  },
]
