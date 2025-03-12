export const roles = ["ADMIN", "USER"] as const;

export type User = {
    id: number
    login: string
    role: typeof roles[number]
}

//table for tests
export const users: User[] = [
    {
        id: 4,
        login: "kris007iron",
        role: "ADMIN"
    },
    {
        id: 5,
        login: "ktostam",
        role: "USER"
    },
    {
        id: 6,
        login: "ktostamtez",
        role: "USER"
    }
]