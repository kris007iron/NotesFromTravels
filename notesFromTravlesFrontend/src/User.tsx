export const roles = ["ADMIN", "USER"] as const;

export type User = {
    id: number
    login: string
    role: typeof roles[number]
}

//table for tests
export const users: User[] = []