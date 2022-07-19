import { MapCreatorLayoutComponent } from './core/layouts/map-creator-layout/map-creator-layout.component';
import { UserLayoutComponent } from './core/layouts/user-layout/user-layout.component';
import { Routes } from '@angular/router';

import { PageNotFoundComponent } from './core/page-not-found/page-not-found.component';
import { LandingComponent } from './landing/landing.component';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';


export const AppRoutes: Routes = [
  {
    path: 'user', component: UserLayoutComponent,
    children: [
      { path: '', loadChildren: () => import('./user/user.module').then(m => m.UserModule) },
    ]
  },
  {
    path: 'creator', component: MapCreatorLayoutComponent,
    children: [
      { path: '', loadChildren: () => import('./map-creator/map-creator.module').then(m => m.MapCreatorModule) },
    ]
  },
  {
    path: '', component: MainLayoutComponent,
    children: [
      { path: '', component: LandingComponent },
      { path: '**', component: PageNotFoundComponent }
    ]
  }
]

// export const AppRoutes: Routes = [
//   {
//     path: '', component: HomeComponent,
//     children: [
//       { path: '', component: LandingComponent },
//       { path: 'user', loadChildren: () => import('./user/user.module').then(m => m.UserModule) },
//       { path: '**', component: PageNotFoundComponent }
//     ], 
//   },
//   {
//     path: 'creator', component: MapCreatorComponent,
//     children: [
//       { path: '', loadChildren: () => import('./map-creator/map-creator.module').then(m => m.MapCreatorModule) },
//     ]
//   },

// ]
