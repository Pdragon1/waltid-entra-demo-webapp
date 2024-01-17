<template>
    <div class="mx-auto max-w-7xl px-4 lg:px-6">
        <div class="mx-auto grid max-w-3xl grid-cols-1 gap-x-10 gap-y-16 sm:gap-y-20 lg:mx-0 lg:max-w-none lg:grid-cols-3">
            <div>
                <h2 class="text-base font-semibold leading-7 text-indigo-600">Credential verified:</h2>
                <p class="mt-2 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">{{ credential.type }}</p>

                <code class="mt-6 text-sm leading-7 text-gray-600">
                    <pre>{{ credential.credential }}</pre>
                </code>

<!--                <p class="mt-6 text-base leading-7 text-gray-600">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Maiores-->
<!--                    impedit perferendis suscipit eaque, iste dolor cupiditate blanditiis ratione.</p>-->
            </div>
            <dl class="col-span-2 grid grid-cols-1 gap-x-8 gap-y-10 text-base leading-7 text-gray-600 sm:grid-cols-2 lg:gap-y-16">
                <div v-for="res in credential.policies" :key="res.policy" class="relative pl-9 bg-gray-50">
                    <dt class="font-semibold text-gray-900">
                        <CheckIcon v-if="res.is_success" aria-hidden="true" class="absolute left-0 top-1 h-5 w-5 text-indigo-500"/>
                        <XMarkIcon v-else aria-hidden="true" class="absolute left-0 top-1 h-5 w-5 text-indigo-500"></XMarkIcon>
                        {{ res.policy }}
                    </dt>
                    <dd class="mt-2">{{ res.description }}</dd>
                    <code class="text-sm text-gray-600"><pre>{{res.result}}</pre></code>
                </div>
            </dl>
        </div>
    </div>
</template>

<script lang="ts" setup>
import {CheckIcon, XMarkIcon} from "@heroicons/vue/20/solid";

const props = defineProps({
    credential: Object
})

console.log("USING CRED: ", JSON.stringify(props.credential, null, 4))

const features = [


    {
        name: 'Invite team members',
        description: 'Rerum repellat labore necessitatibus reprehenderit molestiae praesentium.',
    },
    {name: 'List view', description: 'Corporis asperiores ea nulla temporibus asperiores non tempore assumenda aut.'},
    {
        name: 'Keyboard shortcuts',
        description: 'In sit qui aliquid deleniti et. Ad nobis sunt omnis. Quo sapiente dicta laboriosam.',
    },
    {
        name: 'Calendars',
        description: 'Sed rerum sunt dignissimos ullam. Iusto iure occaecati voluptate eligendi fugiat sequi.',
    },
    {name: 'Notifications', description: 'Quos inventore harum enim nesciunt. Aut repellat rerum omnis adipisci.'},
    {name: 'Boards', description: 'Quae sit sunt excepturi fugit veniam voluptatem ipsum commodi.'},
    {
        name: 'Reporting',
        description: 'Eos laudantium repellat sed architecto earum unde incidunt. Illum sit dolores voluptatem.',
    },
    {
        name: 'Mobile app',
        description: 'Nulla est saepe accusamus nostrum est est. Fugit voluptatum omnis quidem voluptatem.',
    },
]
</script>
