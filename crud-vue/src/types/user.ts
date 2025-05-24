export interface IUser {
    id: number;
    username: string;
    age: number;
    sex: '男' | '女';
    address: string;
    phone: string;
}
// types/user.ts
export interface CurrentUser {
    username: string
    role?: string
    avatar?: string
}

export type UserForm = Omit<IUser, 'id'>;