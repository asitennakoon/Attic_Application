/* An interface for account objects */
export interface IAccount {
    $key?: string,
    username: string,
    password: string,
    email: string,
    store: string,
    type: string,
}
