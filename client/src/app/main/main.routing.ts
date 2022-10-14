import { PostDetailsComponent } from './post-details/post-details.component';
import { LandingComponent } from './landing/landing.component';
import { Routes } from '@angular/router';

export const MainRoutes: Routes = [
  { path: '', component: LandingComponent},
  { path: 'details/:id/:authorId', component: PostDetailsComponent},
];
